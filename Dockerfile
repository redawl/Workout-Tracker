FROM node:26 AS node_base 
ARG VITE_API_DOMAIN
ENV VITE_API_DOMAIN="${VITE_API_DOMAIN}"
COPY frontend/package.json frontend/package-lock.json .
RUN ["npm", "ci"]
COPY frontend .
RUN ["npm", "run", "build:prod"]

FROM golang:1.26
ARG ENVIRONMENT

WORKDIR /usr/src/app
# pre-copy/cache go.mod for pre-downloading dependencies and only redownloading them in subsequent builds if they change
COPY go.mod go.sum ./
RUN ["go", "mod", "download"]

COPY . .
COPY --from=node_base ./out/ ./staticDir
RUN go build -v -o /usr/local/bin/app -tags $ENVIRONMENT .

CMD ["app"]

