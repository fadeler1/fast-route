package com.technical.route.controller;

import com.technical.route.dto.RouteResponse;
import com.technical.route.service.ShortestPathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private final ShortestPathService shortestPathService;

    public RouteController(ShortestPathService shortestPathService) {
        this.shortestPathService = shortestPathService;
    }

    @GetMapping
    public ResponseEntity<RouteResponse> getFastest(@RequestParam String from, @RequestParam String to) {
        RouteResponse resp = shortestPathService.findFastest(from.trim(), to.trim());
        return ResponseEntity.ok(resp);
    }
}
