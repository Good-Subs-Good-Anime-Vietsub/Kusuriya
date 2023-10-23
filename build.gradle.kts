import myaa.subkt.ass.*
import myaa.subkt.tasks.*
import myaa.subkt.tasks.Mux.*
import myaa.subkt.tasks.Nyaa.*
import java.awt.Color
import java.time.*

plugins {
    id("myaa.subkt")
}

subs {
    readProperties("sub.properties")
    episodes(getList("episodes"))

    merge {
        from(get("dialogue")) {
            incrementLayer(12)
        }
        
        if (propertyExists("OP")) {
            from(get("OP")) {
                syncSourceLine("sync")
                syncTargetLine("opsync")
            }
        }

        if (propertyExists("ED")) {
            from(get("ED")) {
                syncSourceLine("sync")
                syncTargetLine("edsync")
            }
        }

        from(getList("TS"))

        includeExtraData(false)
        includeProjectGarbage(false)

        scriptInfo {
                title = "GSGA"
                scaledBorderAndShadow = true
        }

        out(get("mergedname"))
    }

    chapters {
        from(merge.item())
        chapterMarker("chptr")
        out(get("chaptername"))
    }

    mux {
        title(get("title"))

        from(get("raw")) {
            video {
                name("CR")
                lang("jpn")
                default(true)
            }
            audio(0) {
                name("Japanese 2.0 AAC")
                lang("jpn")
                default(true)
            }
            subtitles {
                include(false)
            }
            attachments {
                include(false)
            }
        }

        from(merge.item()) {
            tracks {
                name(get("group"))
                lang("vie")
                default(true)
                forced(false)
                compression(CompressionType.ZLIB)
            }
        }

        chapters(chapters.item()) { lang("vie") }

        attach(get("fonts")) {
            includeExtensions("ttf", "otf", "ttc")
        }
        verifyFonts(true)
        skipUnusedFonts(true)
        onMissingGlyphs(ErrorMode.WARN)
        onFaux(ErrorMode.WARN)
        out(get("muxout"))
    }
}
