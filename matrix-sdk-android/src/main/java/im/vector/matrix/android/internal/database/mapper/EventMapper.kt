/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.matrix.android.internal.database.mapper

import com.squareup.moshi.JsonDataException
import im.vector.matrix.android.api.session.crypto.MXCryptoError
import im.vector.matrix.android.api.session.events.model.Event
import im.vector.matrix.android.api.session.events.model.UnsignedData
import im.vector.matrix.android.api.session.room.send.SendState
import im.vector.matrix.android.internal.crypto.algorithms.olm.OlmDecryptionResult
import im.vector.matrix.android.internal.database.model.EventEntity
import im.vector.matrix.android.internal.di.MoshiProvider
import timber.log.Timber

internal object EventMapper {

    fun map(event: Event, roomId: String): EventEntity {
        val uds = if (event.unsignedData == null) null
        else MoshiProvider.providesMoshi().adapter(UnsignedData::class.java).toJson(event.unsignedData)
        val eventEntity = EventEntity()
        // TODO change this as we shouldn't use event everywhere
        eventEntity.eventId = event.eventId ?: "$$roomId-${System.currentTimeMillis()}-${event.hashCode()}"
        eventEntity.roomId = event.roomId ?: roomId
        eventEntity.content = ContentMapper.map(event.content)
        eventEntity.prevContent = ContentMapper.map(event.resolvedPrevContent())
        eventEntity.isUseless = IsUselessResolver.isUseless(event)
        eventEntity.stateKey = event.stateKey
        eventEntity.type = event.type
        eventEntity.sender = event.senderId
        eventEntity.originServerTs = event.originServerTs
        eventEntity.redacts = event.redacts
        eventEntity.age = event.unsignedData?.age ?: event.originServerTs
        eventEntity.unsignedData = uds
        eventEntity.decryptionResultJson = event.mxDecryptionResult?.let {
            MoshiProvider.providesMoshi().adapter<OlmDecryptionResult>(OlmDecryptionResult::class.java).toJson(it)
        }
        eventEntity.decryptionErrorReason = event.mCryptoErrorReason
        eventEntity.decryptionErrorCode = event.mCryptoError?.name
        return eventEntity
    }

    fun map(eventEntity: EventEntity): Event {
        val ud = eventEntity.unsignedData
                ?.takeIf { it.isNotBlank() }
                ?.let {
                    try {
                        MoshiProvider.providesMoshi().adapter(UnsignedData::class.java).fromJson(it)
                    } catch (t: JsonDataException) {
                        Timber.e(t, "Failed to parse UnsignedData")
                        null
                    }
                }

        return Event(
                type = eventEntity.type,
                eventId = eventEntity.eventId,
                content = ContentMapper.map(eventEntity.content),
                prevContent = ContentMapper.map(eventEntity.prevContent),
                originServerTs = eventEntity.originServerTs,
                senderId = eventEntity.sender,
                stateKey = eventEntity.stateKey,
                roomId = eventEntity.roomId,
                unsignedData = ud,
                redacts = eventEntity.redacts
        ).also {
            it.ageLocalTs = eventEntity.ageLocalTs
            it.sendState = eventEntity.sendState
            eventEntity.decryptionResultJson?.let { json ->
                try {
                    it.mxDecryptionResult = MoshiProvider.providesMoshi().adapter(OlmDecryptionResult::class.java).fromJson(json)
                } catch (t: JsonDataException) {
                    Timber.e(t, "Failed to parse decryption result")
                }
            }
            // TODO get the full crypto error object
            it.mCryptoError = eventEntity.decryptionErrorCode?.let { errorCode ->
                MXCryptoError.ErrorType.valueOf(errorCode)
            }
            it.mCryptoErrorReason = eventEntity.decryptionErrorReason
        }
    }
}

internal fun EventEntity.asDomain(): Event {
    return EventMapper.map(this)
}

internal fun Event.toEntity(roomId: String, sendState: SendState, ageLocalTs: Long?): EventEntity {
    return EventMapper.map(this, roomId).apply {
        this.sendState = sendState
        this.ageLocalTs = ageLocalTs
    }
}