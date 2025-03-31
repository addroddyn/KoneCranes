package trafficsim;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api")
public class GridStreamController {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping(value = "/stream", produces = "text/event-stream")
    public SseEmitter stream() {
        System.out.println("got a request!");
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        System.out.println("Emitters size on request: " + emitters.size());
        return emitter;
    }

    public void pushGrid(Grid grid) {

        for (SseEmitter emitter : emitters) {
            try {
                GridDTO dto = grid.getGridDTO();
                emitter.send(SseEmitter.event()
                        .name("grid")
                        .data(dto));
            } catch (IOException e) {
                System.out.println("Error when sending event: " + e.getMessage());
                e.printStackTrace();
                emitter.complete();
                emitters.remove(emitter);
            }
            catch (IllegalStateException e)
            {
                System.out.println("Error when sending event: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
