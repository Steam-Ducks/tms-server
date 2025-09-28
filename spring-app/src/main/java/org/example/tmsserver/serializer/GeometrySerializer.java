package org.example.tmsserver.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTWriter;

import java.io.IOException;

public class GeometrySerializer extends StdSerializer<Geometry> {

    public GeometrySerializer() {
        super(Geometry.class);
    }

    @Override
    public void serialize(Geometry value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value != null) {
            String wkt = new WKTWriter().write(value);
            gen.writeString(wkt);
        } else {
            gen.writeNull();
        }
    }
}
