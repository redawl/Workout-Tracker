package main

import (
	"net/http"
	"strings"
)

func handleStatic(next http.Handler) http.Handler {
	return http.HandlerFunc(func(response http.ResponseWriter, request *http.Request) {
		path := request.URL.Path
		if strings.HasPrefix(path, "/api") {
			next.ServeHTTP(response, request)
			return
		}
		if path == "/" || strings.HasPrefix(path, "/auth") {
			path = "/index.html"
		}

		serveFrontend(response, request, path)
	})
}
