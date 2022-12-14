/*
 * Copyright 2022 The Matrix.org Foundation C.I.C.
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

package org.matrix.android.sdk.test.fixtures

import org.matrix.android.sdk.internal.database.model.PusherDataEntity
import org.matrix.android.sdk.internal.database.model.PusherEntity

internal object PusherEntityFixture {

    fun aPusherEntity(
            pushKey: String = "",
            kind: String? = null,
            appId: String = "",
            appDisplayName: String? = null,
            deviceDisplayName: String? = null,
            profileTag: String? = null,
            lang: String? = null,
            data: PusherDataEntity? = null,
            enabled: Boolean = true,
            deviceId: String? = null,
    ) = PusherEntity(
            pushKey,
            kind,
            appId,
            appDisplayName,
            deviceDisplayName,
            profileTag,
            lang,
            data,
            enabled,
            deviceId,
    )
}
