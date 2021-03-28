package me.yaman.can.demo.controller

import me.yaman.can.demo.model.CurrentTime
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("time")
class TimeController {
    @GetMapping("/now")
    fun now(): CurrentTime {
        return CurrentTime(Instant.now())
    }
}
