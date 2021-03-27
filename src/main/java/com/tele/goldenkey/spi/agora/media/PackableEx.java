package com.tele.goldenkey.spi.agora.media;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
