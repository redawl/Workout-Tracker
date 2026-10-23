FROM node:latest AS node_base 
ARG VITE_API_DOMAIN
ENV VITE_API_DOMAIN $VITE_API_DOMAIN
COPY frontend .
RUN npm ci
RUN npm run build

FROM golang:1.27
ARG ENVIRONMENT

WORKDIR /usr/src/app
COPY --from=node_base ./out/ ./staticDir

# pre-copy/cache go.mod for pre-downloading dependencies and only redownloading them in subsequent builds if they change
COPY go.mod go.sum ./
RUN go mod download

COPY . .

RUN go build -v -o /usr/local/bin/app -tags $ENVIRONMENT .

CMD ["app"]

