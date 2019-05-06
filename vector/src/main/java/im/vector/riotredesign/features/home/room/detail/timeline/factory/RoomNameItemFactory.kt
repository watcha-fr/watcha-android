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

package im.vector.riotredesign.features.home.room.detail.timeline.factory

import android.text.TextUtils
import im.vector.matrix.android.api.session.events.model.toModel
import im.vector.matrix.android.api.session.room.model.RoomNameContent
import im.vector.matrix.android.api.session.room.timeline.TimelineEvent
import im.vector.riotredesign.R
import im.vector.riotredesign.core.resources.StringProvider
import im.vector.riotredesign.features.home.room.detail.timeline.item.NoticeItem
import im.vector.riotredesign.features.home.room.detail.timeline.item.NoticeItem_

class RoomNameItemFactory(private val stringProvider: StringProvider) {

    fun create(event: TimelineEvent): NoticeItem? {

        val content: RoomNameContent = event.root.content.toModel() ?: return null
        val text = if (!TextUtils.isEmpty(content.name)) {
            stringProvider.getString(R.string.notice_room_name_changed, event.senderName, content.name)
        } else {
            stringProvider.getString(R.string.notice_room_name_removed, event.senderName)
        }
        return NoticeItem_()
                .noticeText(text)
                .avatarUrl(event.senderAvatar)
                .memberName(event.senderName)
    }


}