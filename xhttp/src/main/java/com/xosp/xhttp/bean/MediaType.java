package com.xosp.xhttp.bean;

import android.support.annotation.Nullable;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * use {@link com.google.common.net.MediaType } instead.
 */
//*
@Deprecated
public class MediaType {

    public static final String CHARSET_ATTRIBUTE = "charset";

    public static final String APPLICATION_TYPE = "application";
    public static final String AUDIO_TYPE = "audio";
    public static final String IMAGE_TYPE = "image";
    public static final String TEXT_TYPE = "text";
    public static final String VIDEO_TYPE = "video";

    public static final String WILDCARD = "*";

    private static final String TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)";
    private static final String QUOTED = "\"([^\"]*)\"";
    private static final Pattern TYPE_SUBTYPE
            = Pattern.compile("([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)/([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)");
    private static final Pattern PARAMETER
            = Pattern.compile(";\\s*(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)=(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)|\"([^\"]*)\"))?");
    private final String mediaType;
    private final String type;
    private final String subtype;
    @Nullable
    private final String charset;

    private MediaType(String mediaType, String type, String subtype, @Nullable String charset) {
        this.mediaType = mediaType;
        this.type = type;
        this.subtype = subtype;
        this.charset = charset;
    }

    @Nullable
    public static MediaType parse(String string) {
        Matcher typeSubtype = TYPE_SUBTYPE.matcher(string);
        if (!typeSubtype.lookingAt()) {
            return null;
        } else {
            String type = typeSubtype.group(1).toLowerCase(Locale.US);
            String subtype = typeSubtype.group(2).toLowerCase(Locale.US);
            String charset = null;
            Matcher parameter = PARAMETER.matcher(string);

            for(int s = typeSubtype.end(); s < string.length(); s = parameter.end()) {
                parameter.region(s, string.length());
                if (!parameter.lookingAt()) {
                    return null;
                }

                String name = parameter.group(1);
                if (name != null && name.equalsIgnoreCase("charset")) {
                    String token = parameter.group(2);
                    String charsetParameter;
                    if (token != null) {
                        charsetParameter = token.startsWith("'") && token.endsWith("'") && token.length() > 2 ?
                                token.substring(1, token.length() - 1) : token;
                    } else {
                        charsetParameter = parameter.group(3);
                    }

                    if (charset != null && !charsetParameter.equalsIgnoreCase(charset)) {
                        return null;
                    }

                    charset = charsetParameter;
                }
            }

            return new MediaType(string, type, subtype, charset);
        }
    }

    public String type() {
        return this.type;
    }

    public String subtype() {
        return this.subtype;
    }

    @Nullable
    public Charset charset() {
        return this.charset((Charset)null);
    }

    @Nullable
    public Charset charset(@Nullable Charset defaultValue) {
        try {
            return this.charset != null ? Charset.forName(this.charset) : defaultValue;
        } catch (IllegalArgumentException var3) {
            return defaultValue;
        }
    }

    public String toString() {
        return this.mediaType;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof MediaType && ((MediaType)other).mediaType.equals(this.mediaType);
    }

    public int hashCode() {
        return this.mediaType.hashCode();
    }

}

// */
