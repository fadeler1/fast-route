package com.technical.route.service;

import com.technical.route.dto.RouteResponse;
import com.technical.route.model.Edge;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShortestPathService {

    private final GraphService graphService;

    public ShortestPathService(GraphService graphService) {
        this.graphService = graphService;
    }

    public RouteResponse findFastest(String from, String to) {
        Map<String, List<Edge>> graph = graphService.graph();
        if (graph.isEmpty()) {
            throw new IllegalStateException("Grafo no cargado. Sube un CSV primero.");
        }
        if (!graph.containsKey(from) || !graph.containsKey(to)) {
            throw new NoSuchElementException("Origen o destino inexistente.");
        }
        if (from.equals(to)) {
            return new RouteResponse(List.of(from), 0);
        }

        record State(String node, int dist) {}

        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(State::dist));

        for (String n : graph.keySet()) {
            dist.put(n, Integer.MAX_VALUE);
        }
        dist.put(from, 0);
        pq.add(new State(from, 0));

        while (!pq.isEmpty()) {
            State cur = pq.poll();
            if (cur.dist() != dist.get(cur.node())) continue;
            if (cur.node().equals(to)) break;

            for (Edge e : graph.getOrDefault(cur.node(), List.of())) {
                int nd = cur.dist() + e.time();
                if (nd < dist.get(e.to())) {
                    dist.put(e.to(), nd);
                    prev.put(e.to(), cur.node());
                    pq.add(new State(e.to(), nd));
                }
            }
        }

        if (dist.get(to) == Integer.MAX_VALUE) {
            throw new NoSuchElementException(String.format("No existe ruta entre %s y %s.", from, to));
        }

        LinkedList<String> path = new LinkedList<>();
        for (String at = to; at != null; at = prev.get(at)) {
            path.addFirst(at);
            if (at.equals(from)) break;
        }

        return new RouteResponse(path, dist.get(to));
    }
}
