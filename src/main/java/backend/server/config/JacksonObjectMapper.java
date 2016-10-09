package backend.server.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class JacksonObjectMapper extends ObjectMapper
{
    private static final long serialVersionUID = 1L;

    // default date format for java.util.Date
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public JacksonObjectMapper()
    {
        // write ISO dates
        dateFormat.setLenient(false);
        setDateFormat(dateFormat);
        setTimeZone(TimeZone.getDefault());
        registerModule(new JodaModule());
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // prevent writing BigDecimals in scientific notation
        configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);

        // suppress nulls
        configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        setSerializationInclusion(Include.NON_NULL);

        // output formatting
        configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        configure(SerializationFeature.INDENT_OUTPUT, true);
    }
}
