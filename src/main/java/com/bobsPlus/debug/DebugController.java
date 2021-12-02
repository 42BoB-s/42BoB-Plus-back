package com.bobsPlus.debug;

import com.bobsPlus.dto.RoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class DebugController {

    private final DebugRoomService debugService;

    @PostMapping("/bobs/room/debug_random")
    public ResponseEntity<HashMap<String, Object>> createRoomDebugRandom(RoomDto roomDTO) {

        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();

        long result = debugService.debug_createRoomRandom(roomDTO);

        if (result < 0L) {
            resultMap.put("interCode", (int) result);
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
        } else {
            resultMap.put("interCode", 1);
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        }
        return entity;
    }

    @PostMapping("/bobs/room/debug_data")
    public ResponseEntity<HashMap<String, Object>> createRoomDebugWithData(RoomDto roomDTO,
                                                                           @RequestParam String userId) {
        ResponseEntity<HashMap<String, Object>> entity;
        HashMap<String, Object> resultMap = new HashMap<>();

        long result = debugService.debug_createRoomWithData(roomDTO, userId);

        if (result < 0L) {
            resultMap.put("interCode", (int) result);
            entity = new ResponseEntity<>(resultMap, HttpStatus.FORBIDDEN);
        } else {
            resultMap.put("interCode", 1);
            entity = new ResponseEntity<>(resultMap, HttpStatus.OK);
        }
        return entity;
    }

}

