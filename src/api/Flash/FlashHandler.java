package api.Flash;

import javafx.scene.web.WebEngine;

public class FlashHandler {

    private static final String RUFFLE_WASM_URL = "ruffle/7ba5efac283c4a200e5d.wasm";

    public void injectRuffleScript(WebEngine webEngine) {
        String script = """
            (function() {
                if (!window.RufflePlayer) return;

                // Set a custom path for Ruffle's WASM
                const ruffle = window.RufflePlayer.newest();
                ruffle.config = {
                    wasmLocation: '%s'
                };

                const flashObjects = document.querySelectorAll('object[data$=".swf"], embed[src$=".swf"]');
                flashObjects.forEach((flashObject) => {
                    const parent = flashObject.parentElement;

                    const player = ruffle.createPlayer();
                    parent.replaceChild(player, flashObject);
                    player.load(flashObject.data || flashObject.src);
                });
            })();
        """.formatted(RUFFLE_WASM_URL);

        webEngine.executeScript(script);
    }
}

