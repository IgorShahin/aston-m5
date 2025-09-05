package ru.aston.chain;

import ru.aston.chain.handlers.impl.*;
import ru.aston.chain.model.SignupContext;
import ru.aston.chain.model.SignupRequest;

import java.time.Clock;
import java.util.List;
import java.util.Set;

public class ChainDemo {
    public static void main(String[] args) {
        var taken = Set.of("john.doe@gmail.com"); // уже зарегистрирован
        var pipeline = new Pipeline(
                List.of(
                        new NormalizeEmailHandler(),
                        new BasicValidationHandler(),
                        new PasswordStrengthHandler(),
                        new EmailNotTakenHandler(taken),
                        new BuildUserHandler()
                ),
                Clock.systemUTC()
        );

        var ok = new SignupRequest("  John.Doe+promo@gmail.com ", "Qwerty123");
        var weak = new SignupRequest("user@example.com", "qwerty");
        var dup = new SignupRequest("john.doe@gmail.com", "Strong123");

        run(pipeline, ok);
        run(pipeline, weak);
        run(pipeline, dup);
    }

    private static void run(Pipeline p, SignupRequest req) {
        var res = p.run(SignupContext.of(req));
        switch (res.outcome()) {
            case STOP -> System.out.println("STOP: " + res.reason());
            case CONTINUE -> System.out.println("OK: user=" + res.ctx().user().emailHash());
        }
    }
}
