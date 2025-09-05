package ru.aston.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.chain.handlers.intf.Handler;
import ru.aston.chain.model.SignupContext;

import java.time.Clock;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class Pipeline {
    private static final Logger log = LoggerFactory.getLogger(Pipeline.class);
    private final List<Handler> chain;
    private final Clock clock;

    public Pipeline(List<Handler> chain, Clock clock) {
        this.chain = List.copyOf(chain);
        this.clock = clock;
    }

    public Result run(SignupContext ctx) {
        var cur = ctx;
        for (Handler h : chain) {
            long t0 = System.nanoTime();
            var res = h.handle(cur, clock);
            long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t0);
            log.debug("handler=[{}] outcome=[{}] in [{}] ms{}", h.id(), res.outcome(), ms,
                    res.reason() != null ? " reason=" + res.reason() : "");
            if (res.outcome() == Result.Outcome.STOP) return res;
            cur = res.ctx();
        }
        return Result.next(cur);
    }
}
