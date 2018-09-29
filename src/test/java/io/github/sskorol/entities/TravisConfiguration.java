package io.github.sskorol.entities;

import io.github.sskorol.data.Source;

import java.util.List;
import java.util.Map;

@Source(path = "https://raw.githubusercontent.com/sskorol/test-data-supplier/master/.travis.yml")
public class TravisConfiguration {

    private String language;
    private boolean sudo;
    private boolean install;
    private Map<String, Addon> addons;
    private List<String> jdk;
    private List<String> script;
    private Map<String, List<String>> cache;
    private Map<String, Email> notifications;

    public TravisConfiguration() {
    }

    public TravisConfiguration(final String language, final boolean sudo, final boolean install,
                               final Map<String, Addon> addons, final List<String> jdk, final List<String> script,
                               final Map<String, List<String>> cache, final Map<String, Email> notifications) {
        this.language = language;
        this.sudo = sudo;
        this.install = install;
        this.addons = addons;
        this.jdk = jdk;
        this.script = script;
        this.cache = cache;
        this.notifications = notifications;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public boolean isSudo() {
        return sudo;
    }

    public void setSudo(final boolean sudo) {
        this.sudo = sudo;
    }

    public boolean isInstall() {
        return install;
    }

    public void setInstall(final boolean install) {
        this.install = install;
    }

    public Map<String, Addon> getAddons() {
        return addons;
    }

    public void setAddons(final Map<String, Addon> addons) {
        this.addons = addons;
    }

    public List<String> getJdk() {
        return jdk;
    }

    public void setJdk(final List<String> jdk) {
        this.jdk = jdk;
    }

    public List<String> getScript() {
        return script;
    }

    public void setScript(final List<String> script) {
        this.script = script;
    }

    public Map<String, List<String>> getCache() {
        return cache;
    }

    public void setCache(final Map<String, List<String>> cache) {
        this.cache = cache;
    }

    public Map<String, Email> getNotifications() {
        return notifications;
    }

    public void setNotifications(final Map<String, Email> notifications) {
        this.notifications = notifications;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var that = (TravisConfiguration) o;

        if (sudo != that.sudo) {
            return false;
        }
        if (install != that.install) {
            return false;
        }
        if (language != null ? !language.equals(that.language) : that.language != null) {
            return false;
        }
        if (addons != null ? !addons.equals(that.addons) : that.addons != null) {
            return false;
        }
        if (jdk != null ? !jdk.equals(that.jdk) : that.jdk != null) {
            return false;
        }
        if (script != null ? !script.equals(that.script) : that.script != null) {
            return false;
        }
        if (cache != null ? !cache.equals(that.cache) : that.cache != null) {
            return false;
        }
        return notifications != null ? notifications.equals(that.notifications) : that.notifications == null;
    }

    @Override
    public int hashCode() {
        int result = language != null ? language.hashCode() : 0;
        result = 31 * result + (sudo ? 1 : 0);
        result = 31 * result + (install ? 1 : 0);
        result = 31 * result + (addons != null ? addons.hashCode() : 0);
        result = 31 * result + (jdk != null ? jdk.hashCode() : 0);
        result = 31 * result + (script != null ? script.hashCode() : 0);
        result = 31 * result + (cache != null ? cache.hashCode() : 0);
        result = 31 * result + (notifications != null ? notifications.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TravisConfiguration(" +
            "language=" + language +
            ", sudo=" + sudo +
            ", install=" + install +
            ", addons=" + addons +
            ", jdk=" + jdk +
            ", script=" + script +
            ", cache=" + cache +
            ", notifications=" + notifications +
            ")";
    }
}
