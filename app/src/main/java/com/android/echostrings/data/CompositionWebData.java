package com.android.echostrings.data;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
public class CompositionWebData {
    private static String html="\n" +
            "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\" />\n" +
            "    <title>乐谱渲染</title>\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <link rel=\"stylesheet\" href=\"style.css\"></rel>\n" +
            "    <script src=\"https://cdn.jsdelivr.net/npm/@coderline/alphatab@latest/dist/alphaTab.js\"></script>\n" +
            "    <script src=\"https://kit.fontawesome.com/bc468997fd.js\" crossorigin=\"anonymous\"></script>\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "<div class=\"at-wrap\">\n" +
            "    <div class=\"at-overlay\">\n" +
            "        <div class=\"at-overlay-content\">\n" +
            "            渲染中\n" +
            "        </div>\n" +
            "    </div>\n" +
            "    <div class=\"at-content\">\n" +
            "        <div class=\"at-sidebar\">\n" +
            "            <div class=\"at-sidebar-content\">\n" +
            "                <div class=\"at-track-list\"></div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div class=\"at-viewport\">\n" +
            "            <div class=\"at-main\">\n" +
            "                <div id=\"alphaTab\" data-player-scrolloffsety=\"-30\" data-tex=\"true\"\n" +
            "                     data-player-enableplayer=\"true\"\n" +
            "                     data-player-soundfont=\"https://cdn.jsdelivr.net/npm/@coderline/alphatab@alpha/dist/soundfont/sonivox.sf2\">\n" +
            "                    \\staff{tabs}\n" +
            "                    \\tuning E5 B4 G4 D4 A3 E3\n" +
            "                    (0.3 1.4 1.1 2.2 2.5 1.6).2 (2.4 2.5 1.3).2 (0.5 0.6 0.3).2 (2.1 2.2).4 (0.5).4 (2.4 1.2 2.1).4 (0.5 0.4).2 (0.2).2 (1.1 0.3).4 |\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "    <div id=\"control-bar\" class=\"at-controls\">\n" +
            "        <div style=\"width: 100vw;\">\n" +
            "            <a id=\"next-song\" class=\"unvisible at-player-stop disabled \">\n" +
            "\n" +
            "            </a>\n" +
            "            <a class=\"btn at-player-play-pause disabled\">\n" +
            "                <i class=\"fas fa-play\"></i>\n" +
            "            </a>\n" +
            "            <span class=\"at-player-progress\">0%</span>\n" +
            "            <div class=\"at-song-info\">\n" +
            "                <span class=\"at-song-title\"></span> -\n" +
            "                <span class=\"at-song-artist\"></span>\n" +
            "            </div>\n" +
            "            <div class=\"at-song-position\">00:00 / 00:00</div>\n" +
            "        </div>\n" +
            "        <div style=\"width: 0px;\">\n" +
            "            <a id=\"beat\" class=\"unvisible btn toggle at-count-in\">\n" +
            "                <i class=\"fas fa-hourglass-half\"></i>\n" +
            "            </a>\n" +
            "            <a class=\"unvisible btn at-metronome\">\n" +
            "                <i class=\"fas fa-edit\"></i>\n" +
            "            </a>\n" +
            "            <a id=\"loop\" class=\"unvisible btn at-loop\">\n" +
            "                <i class=\"fas fa-retweet\"></i>\n" +
            "            </a>\n" +
            "            <a id=\"pdf\" class=\"unvisible btn at-print\">\n" +
            "                <i class=\"fas fa-print\"></i>\n" +
            "            </a>\n" +
            "            <div class=\"unvisible at-zoom\">\n" +
            "                <i class=\"fas fa-search\"></i>\n" +
            "                <select>\n" +
            "                    <option value=\"25\">25%</option>\n" +
            "                    <option value=\"50\">50%</option>\n" +
            "                    <option value=\"75\">75%</option>\n" +
            "                    <option value=\"90\">90%</option>\n" +
            "                    <option value=\"100\" selected>100%</option>\n" +
            "                    <option value=\"110\">110%</option>\n" +
            "                    <option value=\"125\">125%</option>\n" +
            "                    <option value=\"150\">150%</option>\n" +
            "                    <option value=\"200\">200%</option>\n" +
            "                </select>\n" +
            "            </div>\n" +
            "            <div class=\"at-layout\">\n" +
            "                <select>\n" +
            "                    <option value=\"horizontal\">水平式</option>\n" +
            "                    <option value=\"page\" selected>翻页式</option>\n" +
            "                </select>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>\n" +
            "\n" +
            "<template id=\"at-track-template\">\n" +
            "    <div class=\"at-track\">\n" +
            "        <div class=\"at-track-icon\">\n" +
            "            <i class=\"fas fa-guitar\"></i>\n" +
            "        </div>\n" +
            "        <div class=\"at-track-details\">\n" +
            "            <div class=\"at-track-name\"></div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</template>\n" +
            "<script src=\"fun.js\">\n" +
            "</script>\n" +
            "</body>\n" +
            "</html>\n" +
            "<script>\n" +
            "        // load elements\n" +
            "        const wrapper = document.querySelector(\".at-wrap\");\n" +
            "      const main = wrapper.querySelector(\".at-main\");\n" +
            "      const texcontent = main.querySelector(\"#alphaTab\"); // tex乐谱内容\n" +
            "      // initialize alphatab\n" +
            "      const settings = {\n" +
            "        //file: \"https://www.alphatab.net/files/canon.gp\",  // 使用自带的卡农乐谱，可以换\n" +
            "        player: {\n" +
            "          enablePlayer: true,\n" +
            "          soundFont: \"https://cdn.jsdelivr.net/npm/@coderline/alphatab@latest/dist/soundfont/sonivox.sf2\",\n" +
            "          scrollElement: wrapper.querySelector('.at-viewport')\n" +
            "        },\n" +
            "      };\n" +
            "      const api = new alphaTab.AlphaTabApi(texcontent, settings);\n" +
            "\n" +
            "      // overlay logic\n" +
            "      const overlay = wrapper.querySelector(\".at-overlay\");\n" +
            "      api.renderStarted.on(() => {\n" +
            "        overlay.style.display = \"flex\";\n" +
            "      });\n" +
            "      api.renderFinished.on(() => {\n" +
            "        overlay.style.display = \"none\";\n" +
            "      });\n" +
            "\n" +
            "      // track selector\n" +
            "      function createTrackItem(track) { // 不同乐器音轨选择\n" +
            "        const trackItem = document\n" +
            "          .querySelector(\"#at-track-template\")\n" +
            "          .content.cloneNode(true).firstElementChild;\n" +
            "        trackItem.querySelector(\".at-track-name\").innerText = track.name;\n" +
            "        trackItem.track = track;\n" +
            "        trackItem.onclick = (e) => {\n" +
            "          e.stopPropagation();\n" +
            "          api.renderTracks([track]);\n" +
            "        };\n" +
            "        return trackItem;\n" +
            "      }\n" +
            "      const trackList = wrapper.querySelector(\".at-track-list\");\n" +
            "      api.scoreLoaded.on((score) => {\n" +
            "        // clear items\n" +
            "        trackList.innerHTML = \"\";\n" +
            "        // generate a track item for all tracks of the score\n" +
            "        score.tracks.forEach((track) => {\n" +
            "          trackList.appendChild(createTrackItem(track));\n" +
            "        });\n" +
            "      });\n" +
            "      api.renderStarted.on(() => {\n" +
            "        // collect tracks being rendered\n" +
            "        const tracks = new Map();\n" +
            "        api.tracks.forEach((t) => {\n" +
            "          tracks.set(t.index, t);\n" +
            "        });\n" +
            "        // mark the item as active or not\n" +
            "        const trackItems = trackList.querySelectorAll(\".at-track\");\n" +
            "        trackItems.forEach((trackItem) => {\n" +
            "          if (tracks.has(trackItem.track.index)) {\n" +
            "            trackItem.classList.add(\"active\");\n" +
            "          } else {\n" +
            "            trackItem.classList.remove(\"active\");\n" +
            "          }\n" +
            "        });\n" +
            "      });\n" +
            "\n" +
            "      /** Controls **/\n" +
            "      api.scoreLoaded.on((score) => {\n" +
            "        wrapper.querySelector(\".at-song-title\").innerText = score.title;\n" +
            "        wrapper.querySelector(\".at-song-artist\").innerText = score.artist;\n" +
            "      });\n" +
            "\n" +
            "      const countIn = wrapper.querySelector('.at-controls .at-count-in');\n" +
            "      countIn.onclick = () => {\n" +
            "        countIn.classList.toggle('active');\n" +
            "        if (countIn.classList.contains('active')) {\n" +
            "          api.countInVolume = 1;\n" +
            "        } else {\n" +
            "          api.countInVolume = 0;\n" +
            "        }\n" +
            "      };\n" +
            "\n" +
            "      const metronome = wrapper.querySelector(\".at-controls .at-metronome\");\n" +
            "      metronome.onclick = () => {\n" +
            "        metronome.classList.toggle(\"active\");\n" +
            "        if (metronome.classList.contains(\"active\")) {\n" +
            "          api.metronomeVolume = 1;\n" +
            "        } else {\n" +
            "          api.metronomeVolume = 0;\n" +
            "        }\n" +
            "      };\n" +
            "\n" +
            "      const loop = wrapper.querySelector(\".at-controls .at-loop\");\n" +
            "      loop.onclick = () => {\n" +
            "        loop.classList.toggle(\"active\");\n" +
            "        api.isLooping = loop.classList.contains(\"active\");\n" +
            "      };\n" +
            "\n" +
            "      wrapper.querySelector(\".at-controls .at-print\").onclick = () => {\n" +
            "        api.print();\n" +
            "      };\n" +
            "\n" +
            "      const zoom = wrapper.querySelector(\".at-controls .at-zoom select\");\n" +
            "      zoom.onchange = () => {\n" +
            "        const zoomLevel = parseInt(zoom.value) / 100;\n" +
            "        api.settings.display.scale = zoomLevel;\n" +
            "        api.updateSettings();\n" +
            "        api.render();\n" +
            "      };\n" +
            "\n" +
            "      const layout = wrapper.querySelector(\".at-controls .at-layout select\");\n" +
            "      layout.onchange = () => {\n" +
            "        switch (layout.value) {\n" +
            "          case \"horizontal\":\n" +
            "            api.settings.display.layoutMode = alphaTab.LayoutMode.Horizontal;\n" +
            "            break;\n" +
            "          case \"page\":\n" +
            "            api.settings.display.layoutMode = alphaTab.LayoutMode.Page;\n" +
            "            break;\n" +
            "        }\n" +
            "        api.updateSettings();\n" +
            "        api.render();\n" +
            "      };\n" +
            "\n" +
            "      // player loading indicator\n" +
            "      const playerIndicator = wrapper.querySelector(\n" +
            "        \".at-controls .at-player-progress\"\n" +
            "      );\n" +
            "      api.soundFontLoad.on((e) => {\n" +
            "        const percentage = Math.floor((e.loaded / e.total) * 100);\n" +
            "        playerIndicator.innerText = percentage + \"%\";\n" +
            "      });\n" +
            "      api.playerReady.on(() => {\n" +
            "        playerIndicator.style.display = \"none\";\n" +
            "      });\n" +
            "\n" +
            "      // main player controls\n" +
            "      const playPause = wrapper.querySelector(\n" +
            "        \".at-controls .at-player-play-pause\"\n" +
            "      );\n" +
            "      const stop = wrapper.querySelector(\".at-controls .at-player-stop\");\n" +
            "      playPause.onclick = (e) => {\n" +
            "        if (e.target.classList.contains(\"disabled\")) {\n" +
            "          return;\n" +
            "        }\n" +
            "        api.playPause();\n" +
            "      };\n" +
            "      stop.onclick = (e) => {\n" +
            "        if (e.target.classList.contains(\"disabled\")) {\n" +
            "          return;\n" +
            "        }\n" +
            "        api.stop();\n" +
            "      };\n" +
            "      api.playerReady.on(() => {\n" +
            "        playPause.classList.remove(\"disabled\");\n" +
            "        stop.classList.remove(\"disabled\");\n" +
            "      });\n" +
            "      api.playerStateChanged.on((e) => {\n" +
            "        const icon = playPause.querySelector(\"i.fas\");\n" +
            "        if (e.state === alphaTab.synth.PlayerState.Playing) {\n" +
            "          icon.classList.remove(\"fa-play\");\n" +
            "          icon.classList.add(\"fa-pause\");\n" +
            "        } else {\n" +
            "          icon.classList.remove(\"fa-pause\");\n" +
            "          icon.classList.add(\"fa-play\");\n" +
            "        }\n" +
            "      });\n" +
            "\n" +
            "      // song position\n" +
            "      function formatDuration(milliseconds) {\n" +
            "        let seconds = milliseconds / 1000;\n" +
            "        const minutes = (seconds / 60) | 0;\n" +
            "        seconds = (seconds - minutes * 60) | 0;\n" +
            "        return (\n" +
            "          String(minutes).padStart(2, \"0\") +\n" +
            "          \":\" +\n" +
            "          String(seconds).padStart(2, \"0\")\n" +
            "        );\n" +
            "      }\n" +
            "\n" +
            "      const songPosition = wrapper.querySelector(\".at-song-position\");\n" +
            "      let previousTime = -1;\n" +
            "      api.playerPositionChanged.on((e) => {\n" +
            "        // reduce number of UI updates to second changes.\n" +
            "        const currentSeconds = (e.currentTime / 1000) | 0;\n" +
            "        if (currentSeconds == previousTime) {\n" +
            "          return;\n" +
            "        }\n" +
            "\n" +
            "        songPosition.innerText =\n" +
            "          formatDuration(e.currentTime) + \" / \" + formatDuration(e.endTime);\n" +
            "      });\n" +
            "</script>\n" +
            "<style>\n" +
            "body {\n" +
            "    width: 100%;\n" +
            "    height: 100%;\n" +
            "    margin: 0;\n" +
            "    padding: 0;\n" +
            "    font-family: Arial, Helvetica, sans-serif;\n" +
            "    font-size: 12px;\n" +
            "  }\n" +
            "\n" +
            "  .at-wrap {\n" +
            "    width: 100vw;\n" +
            "    height: 100vh;\n" +
            "    margin: 0 auto;\n" +
            "    border: 0px solid rgba(0, 0, 0, 0.12);\n" +
            "    display: flex;\n" +
            "    flex-direction: column;\n" +
            "    overflow: hidden;\n" +
            "    position: relative;\n" +
            "  }\n" +
            "\n" +
            "  .at-content {\n" +
            "    position: relative;\n" +
            "    overflow: hidden;\n" +
            "    flex: 1 1 auto;\n" +
            "  }\n" +
            "\n" +
            "  /** Sidebar **/\n" +
            "  .at-sidebar {\n" +
            "    display: none;\n" +
            "  }\n" +
            "\n" +
            "  .at-sidebar:hover {\n" +
            "    max-width: 400px;\n" +
            "    transition: max-width 0.2s;\n" +
            "    overflow-y: auto;\n" +
            "  }\n" +
            "\n" +
            "  .at-viewport {\n" +
            "    overflow-y: auto;\n" +
            "    position: absolute;\n" +
            "    top: 0;\n" +
            "    left: 0px;\n" +
            "    right: 0;\n" +
            "    bottom: 0;\n" +
            "    padding-right: 20px;\n" +
            "  }\n" +
            "\n" +
            "  .at-footer {\n" +
            "    flex: 0 0 auto;\n" +
            "    background: #fdb904;\n" +
            "    color: #fff;\n" +
            "  }\n" +
            "\n" +
            "  /** Overlay **/\n" +
            "\n" +
            "  .at-overlay {\n" +
            "    /** Fill Parent */\n" +
            "    position: absolute;\n" +
            "    top: 0;\n" +
            "    left: 0;\n" +
            "    right: 0;\n" +
            "    bottom: 0;\n" +
            "    z-index: 1002;\n" +
            "\n" +
            "    /* Blurry dark shade */\n" +
            "    backdrop-filter: blur(3px);\n" +
            "    background: rgba(0, 0, 0, 0.5);\n" +
            "\n" +
            "    /** center content */\n" +
            "    display: flex;\n" +
            "    justify-content: center;\n" +
            "    align-items: flex-start;\n" +
            "  }\n" +
            "\n" +
            "  .at-overlay-content {\n" +
            "    /* white box with drop-shadow */\n" +
            "    margin-top: 20px;\n" +
            "    background: #fff;\n" +
            "    box-shadow: 0px 5px 10px 0px rgba(0, 0, 0, 0.3);\n" +
            "    padding: 10px;\n" +
            "  }\n" +
            "\n" +
            "  /** Track selector **/\n" +
            "  .at-track {\n" +
            "    display: flex;\n" +
            "    position: relative;\n" +
            "    padding: 5px;\n" +
            "    transition: background 0.2s;\n" +
            "    cursor: pointer;\n" +
            "  }\n" +
            "\n" +
            "  .at-track:hover {\n" +
            "    background: rgba(0, 0, 0, 0.1);\n" +
            "  }\n" +
            "\n" +
            "  .at-track > .at-track-icon,\n" +
            "  .at-track > .at-track-details {\n" +
            "    display: flex;\n" +
            "    flex-direction: column;\n" +
            "    justify-content: center;\n" +
            "  }\n" +
            "\n" +
            "  .at-track > .at-track-icon {\n" +
            "    flex-shrink: 0;\n" +
            "    font-size: 32px;\n" +
            "    opacity: 0.5;\n" +
            "    transition: opacity 0.2s;\n" +
            "    width: 64px;\n" +
            "    height: 64px;\n" +
            "    margin-right: 5px;\n" +
            "    align-items: center;\n" +
            "  }\n" +
            "\n" +
            "  .at-track-name {\n" +
            "    font-weight: bold;\n" +
            "    margin-bottom: 5px;\n" +
            "  }\n" +
            "\n" +
            "  .at-track:hover > .at-track-icon {\n" +
            "    opacity: 0.8;\n" +
            "  }\n" +
            "\n" +
            "  .at-track.active {\n" +
            "    background: rgba(0, 0, 0, 0.03);\n" +
            "  }\n" +
            "\n" +
            "  .at-track.active > .at-track-icon {\n" +
            "    color: #fdb904;\n" +
            "    opacity: 1;\n" +
            "  }\n" +
            "\n" +
            "  .at-track > .at-track-name {\n" +
            "    font-weight: 500;\n" +
            "  }\n" +
            "\n" +
            "  /** Footer **/\n" +
            "  .at-controls {\n" +
            "    flex: 0 0 auto;\n" +
            "    display: flex;\n" +
            "    justify-content: space-between;\n" +
            "    background: #fdb904;\n" +
            "    color: #fff;\n" +
            "  }\n" +
            "\n" +
            "  .at-controls > div {\n" +
            "    display: flex;\n" +
            "    justify-content: flex-start;\n" +
            "    align-content: center;\n" +
            "    align-items: center;\n" +
            "  }\n" +
            "\n" +
            "  .at-controls > div > * {\n" +
            "    display: flex;\n" +
            "    text-align: center;\n" +
            "    align-items: center;\n" +
            "    justify-content: center;\n" +
            "    cursor: pointer;\n" +
            "    padding: 4px;\n" +
            "    margin: 0 3px;\n" +
            "  }\n" +
            "\n" +
            "  .at-controls .btn {\n" +
            "    color: #fff;\n" +
            "    border-radius: 0;\n" +
            "    height: 40px;\n" +
            "    width: 40px;\n" +
            "    height: 40px;\n" +
            "    font-size: 16px;\n" +
            "  }\n" +
            "  .at-controls .btn.disabled {\n" +
            "    cursor: progress;\n" +
            "    opacity: 0.5;\n" +
            "  }\n" +
            "\n" +
            "  .at-controls a.active {\n" +
            "    background: #5588c7;\n" +
            "    text-decoration: none;\n" +
            "  }\n" +
            "\n" +
            "  .at-controls .btn i {\n" +
            "    vertical-align: top;\n" +
            "  }\n" +
            "\n" +
            "  .at-controls select {\n" +
            "    -moz-appearance: none;\n" +
            "    -webkit-appearance: none;\n" +
            "    appearance: none;\n" +
            "    border: none;\n" +
            "    width: 100%;\n" +
            "    height: 40px;\n" +
            "    background: #fdb904;\n" +
            "    padding: 4px 10px;\n" +
            "    color: #fff;\n" +
            "    font-size: 16px;\n" +
            "    text-align-last: center;\n" +
            "    text-align: center;\n" +
            "    -ms-text-align-last: center;\n" +
            "    -moz-text-align-last: center;\n" +
            "    cursor: pointer;\n" +
            "  }\n" +
            "\n" +
            "  .at-song-title {\n" +
            "    font-weight: bold;\n" +
            "  }\n" +
            "\n" +
            "  .at-cursor-bar {\n" +
            "    /* Defines the color of the bar background when a bar is played */\n" +
            "    background: rgba(255, 242, 0, 0.25);\n" +
            "  }\n" +
            "\n" +
            "  .at-selection div {\n" +
            "    /* Defines the color of the selection background */\n" +
            "    background: rgba(64, 64, 255, 0.1);\n" +
            "  }\n" +
            "\n" +
            "  .at-cursor-beat {\n" +
            "    /* Defines the beat cursor */\n" +
            "    background: rgba(64, 64, 255, 0.75);\n" +
            "    width: 3px;\n" +
            "  }\n" +
            "\n" +
            "  .at-highlight * {\n" +
            "    /* Defines the color of the music symbols when they are being played (svg) */\n" +
            "    fill: #0078ff;\n" +
            "    stroke: #0078ff;\n" +
            "  }\n" +
            "  unvisible {\n" +
            "    display: none;\n" +
            "    width: 0px;\n" +
            "    height: 0px;\n" +
            "  }\n" +
            "</style>\n" +
            "\n";
    public static Map<String,String>guitar_id=new HashMap<>();
    static {
        guitar_id.put("尼龙弦吉他","24");
        guitar_id.put("钢弦吉他","25");
        guitar_id.put("爵士电吉他","26");
        guitar_id.put("清音电吉他","27");
        guitar_id.put("闷音电吉他","28");
        guitar_id.put("加驱动效果的电吉他","29");
        guitar_id.put("加失真效果的电吉他","30");
        guitar_id.put("吉他和音","31");

    }
    public static String getId(String tone){
        return guitar_id.get(tone);
    }
    /**
     * need input the tex
     * @param tex
     * @return
     */
    public static String getUpdatedHtml(String tex){
        Document doc = Jsoup.parse(html);
        Element alphaTab_element = doc.getElementById("alphaTab");
        alphaTab_element.text(tex);
        return doc.html();
    }
    public static String getHtmlTex(String html){
        Document doc = Jsoup.parse(html);
        Element alphaTab_element = doc.getElementById("alphaTab");
        return alphaTab_element.text();
    }
    public static String getDefaultTex(){
        return getHtmlTex(html);
    }
}
