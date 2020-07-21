#!/usr/bin/groovy
package com.psl.stages

import com.psl.Pipeline

def healthcheck(Pipeline p) {
    def hostIp = sh script: "ip route show | awk '/default/ {print \$3}' | tr -d '\n'", returnStdout: true

    timeout(time: p.timeout_hc, unit: 'SECONDS'){
        waitUntil(quiet: true) {
            def response = sh script: "curl ${hostIp}:${p.app_port}/ping", returnStdout: true
            if (response != "pong!"){
                error("ERROR102 - Service is Unhealthy for last ${p.timeout_hc} Second")
            } else {
                println "Service is Healthy :D"
                return true
            }
        }
    }
}