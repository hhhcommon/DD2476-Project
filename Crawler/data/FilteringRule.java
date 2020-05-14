23
https://raw.githubusercontent.com/datastax/metric-collector-for-apache-cassandra/master/src/main/java/com/datastax/mcac/FilteringRule.java
package com.datastax.mcac;

import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class FilteringRule
{
    public static final String ALLOW = "allow";
    public static final String DENY = "deny";
    public static final String GLOBAL = "global";
    public static final String DATALOG_ONLY = "datalog";

    public static final FilteringRule FILTERED_GLOBALLY = new FilteringRule(DENY, ".*", GLOBAL);
    public static final FilteringRule FILTERED_INSIGHTS = new FilteringRule(DENY, ".*", DATALOG_ONLY);
    public static final FilteringRule ALLOWED_GLOBALLY = new FilteringRule(ALLOW, ".*", GLOBAL);
    public static final FilteringRule ALLOWED_INSIGHTS = new FilteringRule(ALLOW, ".*", DATALOG_ONLY);

    @JsonProperty("policy")
    public final String policy;

    @JsonProperty("pattern")
    public final String patternStr;

    @JsonProperty("scope")
    public final String scope;

    @JsonIgnore
    private final Supplier<Pattern> pattern;
    @JsonIgnore
    public final boolean isAllowRule;
    @JsonIgnore
    public final boolean isGlobal;

    @JsonCreator
    public FilteringRule(@JsonProperty("policy") String policy, @JsonProperty("pattern") String patternStr, @JsonProperty("scope") String scope)
    {
        if (!(policy.equalsIgnoreCase(ALLOW) || policy.equalsIgnoreCase(DENY)))
            throw new IllegalArgumentException(String.format("Policy must be '%s' or '%s'", ALLOW, DENY));

        if (!(scope.equalsIgnoreCase(GLOBAL) || scope.equalsIgnoreCase(DATALOG_ONLY)))
            throw new IllegalArgumentException(String.format("Scope must be '%s' or '%s'", GLOBAL, DATALOG_ONLY));

        this.scope = scope;
        this.isGlobal = scope.equalsIgnoreCase(GLOBAL);
        this.policy = policy;
        this.isAllowRule = policy.equalsIgnoreCase(ALLOW);
        this.patternStr = patternStr;

        this.pattern = Suppliers.memoize(() ->
        {
            try
            {
                return Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
            }
            catch (PatternSyntaxException e)
            {
                throw new IllegalArgumentException("Invalid pattern: " + patternStr, e);
            }
        });
    }

    public boolean isAllowed(String name)
    {
        boolean match = pattern.get().matcher(name).find();
        return isAllowRule ? match : !match;
    }

    /**
     * Returns the most applicable filtering rule for this name
     * taking into account global vs insights level scope.
     *
     * global taking precedent over insights and deny rules taking precedent over allow rules
     * @param name
     * @return The rule that applied for this name.
     */
    public static FilteringRule applyFilters(String name, Collection<FilteringRule> rules)
    {
        FilteringRule allowRule = null;
        FilteringRule denyRule = null;
        boolean hasDenyRule = false;
        boolean hasAllowRule = false;

        for (FilteringRule rule : rules)
        {
            if (rule.isAllowRule)
            {
                hasAllowRule = true;
                if ((allowRule == null || !allowRule.isGlobal) && rule.isAllowed(name))
                {
                    allowRule = rule;
                }
            }
            else
            {
                hasDenyRule = true;
                if ((denyRule == null || !denyRule.isGlobal) && !rule.isAllowed(name))
                {
                    denyRule = rule;
                }
            }
        }

        if (rules.isEmpty())
            return FilteringRule.ALLOWED_GLOBALLY;

        if (denyRule != null)
            return denyRule;

        if (allowRule != null)
            return allowRule;

        if (hasDenyRule && !hasAllowRule)
            return FilteringRule.ALLOWED_GLOBALLY;

        return FilteringRule.FILTERED_GLOBALLY;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilteringRule rule = (FilteringRule) o;
        return Objects.equals(policy, rule.policy) &&
                Objects.equals(patternStr, rule.patternStr) &&
                Objects.equals(scope, rule.scope);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(policy, patternStr, scope);
    }


    @Override
    public String toString()
    {
        return "FilteringRule{" +
                "policy='" + policy + '\'' +
                ", patternStr='" + patternStr + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
