package com.technical.route;

import com.technical.route.dto.RouteResponse;
import com.technical.route.service.GraphService;
import com.technical.route.service.ShortestPathService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class ShortestPathServiceTest {

    GraphService graphService;
    ShortestPathService sps;

    @BeforeEach
    void setup() {
        graphService = new GraphService();
        sps = new ShortestPathService(graphService);
        String csv = """
      loc_start;loc_end;time
      R11;R12;20
      R12;R13;9
      R13;R12;11
      R13;R20;9
      R20;R13;11
      CP1;R11;84
      R11;CP1;92
      CP1;CP2;7
      CP2;CP1;10
      CP2;R20;67
      R20;CP2;60
      """;
        graphService.loadCsv(new java.io.ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void findsFastestRoute() {
        RouteResponse r = sps.findFastest("CP1", "R20");
        assertEquals(74, r.tiempoTotal());
        assertEquals(java.util.List.of("CP1", "CP2", "R20"), r.ruta());
    }

    @Test
    void sameNodeZero() {
        RouteResponse r = sps.findFastest("CP1", "CP1");
        assertEquals(0, r.tiempoTotal());
        assertEquals(java.util.List.of("CP1"), r.ruta());
    }
}
