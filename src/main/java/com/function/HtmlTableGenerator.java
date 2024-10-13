package com.function;



import org.apache.commons.lang3.StringEscapeUtils;

public class HtmlTableGenerator {
    public static String generate(DataModel data) {
        StringBuilder html = new StringBuilder();
        html.append("<table>");
        html.append("<tr><th>Name</th><th>Value</th></tr>");
        html.append("<tr>");
        html.append("<td>").append(StringEscapeUtils.escapeHtml4(data.getName())).append("</td>");
        html.append("<td>").append(StringEscapeUtils.escapeHtml4(data.getValue())).append("</td>");
        html.append("</tr>");
        html.append("</table>");
        return html.toString();
    }
}
