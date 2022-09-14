package com.biit.webforms.exporters.dotgraph;

import com.biit.form.entity.BaseRepeatableGroup;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Base abstract class to generate dot graph code for forms and filtered forms
 *
 * @param <T>
 */
public abstract class ExporterDotFormBasic<T> extends ExporterDot<T> {

    protected String generateDotRule(Flow rule) {
        String dotRule = "";
        if (rule == null || rule.getOrigin() == null) {
            return "";
        }
        String origin = getDotId(rule.getOrigin());
        String destiny = null;
        String label;

        if (rule.isOthers()) {
            label = "OTHERS";
        } else {
            label = filterDotLanguage(rule.getConditionStringWithFormat());
        }

        switch (rule.getFlowType()) {
            case NORMAL:
                destiny = getDotId(rule.getDestiny());
                break;
            case END_LOOP:
                BaseRepeatableGroup group = rule.getOrigin().getRepeatableGroup();
                if (group == null || group.getChildren().isEmpty()) {
                    return "";
                }
                destiny = getDotId(group.getChildren().get(0));
                break;
            case END_FORM:
                destiny = "end";
                break;
        }

        dotRule += "\t" + origin + " -> " + destiny + " [label = \"" + label + "\", fontcolor=" + getFontColor(rule.isReadOnly())
                + ", color=" + getLinkColor(rule.isReadOnly()) + ", penwidth=" + getPenWidth() + "];\n";

        return dotRule;
    }

    protected String createLegend(Form form) {
        return "\tsubgraph cluster_0{rank = sink; Legend [shape=none, margin=0, label=<<table border=\"0\"><tr><td>" + form.getLabel()
                + "</td></tr><tr><td>version " + form.getVersion() + " (" + getTimestampFormattedString(form.getUpdateTime())
                + ")</td></tr></table>> ]}\n";
    }

    protected String getTimestampFormattedString(Timestamp timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat();
        return formatter.format(timestamp);
    }

    protected String getDotId(TreeObject node) {
        return "id_" + filterDotLanguageId(node.getComparationId());
    }

}
