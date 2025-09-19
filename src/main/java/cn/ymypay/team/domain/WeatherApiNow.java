package cn.ymypay.team.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * @author ljf
 */
@Data
public class WeatherApiNow {
    public Location location;
    public Current current;

    @Data
    public static class Location {
        public String name;
        public String region;
        public String country;
        public Double lat;
        public Double lon;
        @JSONField(name = "tz_id")
        public String tzId;
        @JSONField(name = "localtime_epoch")
        public Integer localtimeEpoch;
        public String localtime;
    }

    @Data
    public static class Current {
        @JSONField(name = "last_updated_epoch")
        public Integer lastUpdatedEpoch;
        @JSONField(name = "last_updated")
        public String lastUpdated;
        @JSONField(name = "temp_c")
        public Double tempC;
        @JSONField(name = "temp_f")
        public Double tempF;
        @JSONField(name = "is_day")
        public Integer isDay;
        public Condition condition;
        @JSONField(name = "wind_mph")
        public Double windMph;
        @JSONField(name = "wind_kph")
        public Double windKph;
        @JSONField(name = "wind_degree")
        public Integer windDegree;
        @JSONField(name = "wind_dir")
        public String windDir;
        @JSONField(name = "pressure_mb")
        public Double pressureMb;
        @JSONField(name = "pressure_in")
        public Double pressureIn;
        @JSONField(name = "precip_mm")
        public Double precipMm;
        @JSONField(name = "precip_in")
        public Double precipIn;
        public Integer humidity;
        public Integer cloud;
        @JSONField(name = "feelslike_c")
        public Double feelslikeC;
        @JSONField(name = "feelslike_f")
        public Double feelslikeF;
        @JSONField(name = "windchill_c")
        public Double windchillC;
        @JSONField(name = "windchill_f")
        public Double windchillF;
        @JSONField(name = "heatindex_c")
        public Double heatindexC;
        @JSONField(name = "heatindex_f")
        public Double heatindexF;
        @JSONField(name = "dewpoint_c")
        public Double dewpointC;
        @JSONField(name = "dewpoint_f")
        public Double dewpointF;
        @JSONField(name = "vis_km")
        public Double visKm;
        @JSONField(name = "vis_miles")
        public Double visMiles;
        public Double uv;
        @JSONField(name = "gust_mph")
        public Double gustMph;
        @JSONField(name = "gust_kph")
        public Double gustKph;
    }

    @Data
    public static class Condition {
        public String text;
        public String icon;
        public Integer code;
    }
}
