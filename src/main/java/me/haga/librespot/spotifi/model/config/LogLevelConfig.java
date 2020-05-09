package me.haga.librespot.spotifi.model.config;

import java.io.Serializable;

public class LogLevelConfig implements Serializable {

    private LogLevelGui level;

    public LogLevelConfig() {

    }


    public LogLevelConfig(String level) {
        this.level = new LogLevelGui(level);
    }

    public LogLevelGui getLevel() {
        return level;
    }

    public void setLevel(LogLevelGui level) {
        this.level = level;
    }

    public class LogLevelGui {
        private String root;

        public LogLevelGui() {
        }

        public LogLevelGui(String level) {
            this.root = level;
        }

        public String getRoot() {
            return root;
        }

        public void setRoot(String root) {
            this.root = root;
        }
    }
}
