/*
 * Copyright 2013-2019 Xia Jun(3979434@qq.com).
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
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.cn.graduationclient.cim.coder;

import com.cn.graduationclient.cim.constant.CIMConstant;
import com.cn.graduationclient.cim.model.Protobufable;
import com.cn.graduationclient.cim.model.SentBody;

import java.nio.ByteBuffer;

public class ClientMessageEncoder {

    public ByteBuffer encode(Protobufable body) {

        byte[] data = body.getByteArray();

        ByteBuffer buffer = ByteBuffer.allocate(data.length + CIMConstant.DATA_HEADER_LENGTH);

        buffer.put(createHeader(body.getType(), data.length));
        buffer.put(data);
        buffer.flip();

        return buffer;

    }

    /**
     * 消息体最大为65535
     */
    private byte[] createHeader(byte type, int length) {
        byte[] header = new byte[CIMConstant.DATA_HEADER_LENGTH];
        header[0] = type;
        header[1] = (byte) (length & 0xff);
        header[2] = (byte) ((length >> 8) & 0xff);
        return header;
    }

}
