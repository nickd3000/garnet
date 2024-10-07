package com.physmo.garnet.toolkit

import com.physmo.garnet.Display
import com.physmo.garnet.graphics.Texture
import spock.lang.Specification

class InlineTextureTest extends Specification {

    def "cds"() {
        given:
            Display display = new Display(400, 400)
            display.init()

            String data =
                    "    x   " +
                            "    x   " +
                            "    x   " +
                            "    x   " +
                            "        " +
                            "        " +
                            "        " +
                            "        "

        when:
            Texture result = InlineTexture.create(data, 8, 8)

        then:
            result.getWidth() == 8
            result.getHeight() == 8
    }
}
