/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.bluetoothlegatt;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes
{

    private static HashMap<String, String> attributes = new HashMap();

    public static String SERVICE_GUID = "0000fee0-0000-1000-8000-00805f9b34fb";
    public static String CHARACTERISTIC_GUID = "00000007-0000-3512-2118-0009af100700";

    public static String CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb";

    static
    {
        // Sample Services.
        attributes.put("0000fee0-0000-1000-8000-00805f9b34fb", "Test Service");
    }

    public static String lookup(String uuid, String defaultName)
    {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
