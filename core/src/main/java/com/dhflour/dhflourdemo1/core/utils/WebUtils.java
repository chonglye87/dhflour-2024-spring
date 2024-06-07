package com.dhflour.dhflourdemo1.core.utils;

import io.github.mngsk.devicedetector.Detection;
import io.github.mngsk.devicedetector.DeviceDetector;
import io.github.mngsk.devicedetector.client.Client;
import io.github.mngsk.devicedetector.device.Device;
import io.github.mngsk.devicedetector.operatingsystem.OperatingSystem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class WebUtils {

    public static String getClientIPv4(ServerWebExchange exchange) {
        String ip = null;
        // X-Forwarded-For 헤더 확인
        String forwardedHeader = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (forwardedHeader != null) {
            ip = forwardedHeader.split(",")[0].trim();
        } else if (exchange.getRequest().getRemoteAddress() != null) {
            ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        }
        // IP 주소가 IPv4 형인지 확인
        if (ip != null) {
            try {
                InetAddress inetAddress = InetAddress.getByName(ip);
                if (inetAddress instanceof java.net.Inet4Address) {
                    return ip; // IPv4 주소 반환
                }
            } catch (UnknownHostException e) {
                // 로그에 예외 정보 기록
                log.error("IP Address conversion error: " + e.getMessage(), e);
            }
        }
        return "IPv4 address not available";
    }

    public static String getClientIp(ServerWebExchange exchange) {
        String forwardedHeader = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (forwardedHeader != null) {
            // X-Forwarded-For 헤더가 존재하면 첫 번째 IP 주소를 사용합니다. 이는 클라이언트의 원래 IP 주소입니다.
            return forwardedHeader.split(",")[0].trim();
        } else if (exchange.getRequest().getRemoteAddress() != null) {
            // X-Forwarded-For 헤더가 없으면 연결 요청의 IP 주소를 반환합니다.
            return exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        }
        return "IP not available";
    }

    /**
     * Device Detector
     * @param exchange ServerWebExchange
     */
    public static void getDeviceDetector(ServerWebExchange exchange) {
        DeviceDetector dd = new DeviceDetector.DeviceDetectorBuilder().build();
        final String userAgent = exchange.getRequest().getHeaders().getFirst("User-Agent");
        log.info("userAgent: {}", userAgent);
        Detection detection = dd.detect(userAgent);
        log.info("Device : {}", detection.getDevice().map(Device::toString).orElse("unknown"));
        log.info("OS : {}", detection.getOperatingSystem().map(OperatingSystem::toString).orElse("unknown"));
        log.info("Client : {}", detection.getClient().map(Client::toString).orElse("unknown"));
        if (detection.getDevice().isPresent()) {
            log.info("Device Type: {}", detection.getDevice().get().getType()); // bot, browser, feed reader...
            log.info("Device Brand : {}", detection.getDevice().get().getBrand().orElse("unknown"));
            log.info("Device Model : {}", detection.getDevice().get().getModel().orElse("unknown"));
        }
        if (detection.isBot()) {
            log.info("7 : {}", detection.getClient().get().getName().get());
        }
    }
}
