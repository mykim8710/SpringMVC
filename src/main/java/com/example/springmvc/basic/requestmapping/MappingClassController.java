package com.example.springmvc.basic.requestmapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/mapping/members")
public class MappingClassController {

    /**
     * GET /mapping/members
     */
    @GetMapping
    public String findMembers() {
        log.info("[GET] /mapping/members");
        return "find members";
    }

    /**
     * POST /mapping/members
     */
    @PostMapping
    public String addMember() {
        log.info("[POST] /mapping/members");
        return "add member";
    }

    /**
     * GET /mapping/members/{memberId}
     */
    @GetMapping("/{memberId}")
    public String findMemberById(@PathVariable Long memberId) {
        log.info("[GET] /mapping/members/{}", memberId);
        return "find member by id";
    }

    /**
     * PATCH /mapping/members/{memberId}
     */
    @PatchMapping("/{memberId}")
    public String editMemberById(@PathVariable Long memberId) {
        log.info("[PATCH] /mapping/members/{}", memberId);
        return "edit member by id";
    }

    /**
     * DELETE /mapping/members/{memberId}
     */
    @DeleteMapping("/{memberId}")
    public String removeMemberById(@PathVariable Long memberId) {
        log.info("[DELETE] /mapping/members/{}", memberId);
        return "remove member by id";
    }

}
