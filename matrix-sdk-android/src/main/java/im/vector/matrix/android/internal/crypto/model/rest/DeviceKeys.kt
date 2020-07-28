/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.matrix.android.internal.crypto.model.rest

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeviceKeys(
        /**
         * Required. The ID of the user the device belongs to. Must match the user ID used when logging in.
         */
        @Json(name = "user_id")
        val userId: String,

        /**
         * Required. The ID of the device these keys belong to. Must match the device ID used when logging in.
         */
        @Json(name = "device_id")
        val deviceId: String,

        /**
         * Required. The encryption algorithms supported by this device.
         */
        @Json(name = "algorithms")
        val algorithms: List<String>?,

        /**
         * Required. Public identity keys. The names of the properties should be in the format <algorithm>:<device_id>.
         * The keys themselves should be encoded as specified by the key algorithm.
         */
        @Json(name = "keys")
        val keys: Map<String, String>?,

        /**
         * Required. Signatures for the device key object. A map from user ID, to a map from <algorithm>:<device_id> to the signature.
         * The signature is calculated using the process described at https://matrix.org/docs/spec/appendices.html#signing-json.
         */
        @Json(name = "signatures")
        val signatures: Map<String, Map<String, String>>?
)