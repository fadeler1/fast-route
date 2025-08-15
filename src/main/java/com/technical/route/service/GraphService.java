package com.technical.route.service;

import com.technical.route.model.Edge;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class GraphService {

    private final AtomicReference<Map<String, List<Edge>>> graphRef =
            new AtomicReference<>(Collections.emptyMap());

    public void loadCsv(InputStream in) {
        Map<String, List<Edge>> next = new HashMap<>(16384);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            long lineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("loc_start")) continue;

                String[] parts = line.split(";");
                if (parts.length != 3) {
                    throw new IllegalArgumentException("Línea " + lineNo + " inválida: " + line);
                }
                String from = parts[0].trim();
                String to = parts[1].trim();
                int time;
                try {
                    time = Integer.parseInt(parts[2].trim());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Tiempo inválido en línea " + lineNo + ": " + parts[2]);
                }

                next.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, time));
                // Registrar nodos que solo aparecen como destino
                next.computeIfAbsent(to, k -> new ArrayList<>());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar CSV: " + e.getMessage(), e);
        }

        next.replaceAll((k, v) -> Collections.unmodifiableList(v));
        graphRef.set(Collections.unmodifiableMap(next));
    }

    public Map<String, List<Edge>> graph() {
        return graphRef.get();
    }

    public boolean isEmpty() {
        return graphRef.get().isEmpty();
    }
}
