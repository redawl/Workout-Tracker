package main

import (
	"bufio"
	"errors"
	"log"
	"os"
	"strings"
)

const (
	PostgresUser = "POSTGRES_USER"
	PostgresPass = "POSTGRES_PASS"
	PostgresDB   = "POSTGRES_DB"
	APIDomain    = "API_DOMAIN"
)

type Env struct {
	PostgresUser string
	PostgresPass string
	PostgresDB   string
	APIDomain    string
}

func GetEnv() *Env {
	fileEnv := getFromFile(".env")
	if fileEnv != nil {
		return fileEnv
	}

	environEnv := getFromEnvironment(os.Environ())

	return environEnv
}

func getFromFile(filename string) *Env {
	env := &Env{}

	f, err := os.Open(filename)
	if err != nil {
		if !errors.Is(err, os.ErrNotExist) {
			log.Printf("Warning: error reading from .env, ignoring: %v\n", err)
		}

		return nil
	}

	scanner := bufio.NewScanner(f)
	i := 0
	for scanner.Scan() {
		defer func() { i++ }()
		line := scanner.Text()

		line = strings.TrimSuffix(line, "\n")
		if len(line) == 0 {
			continue
		}

		key, value, found := strings.Cut(line, "=")
		if !found {
			log.Printf("Warning line %d: line has no =\n", i)
			continue
		}
		switch key {
		case PostgresUser:
			env.PostgresUser = value
		case PostgresPass:
			env.PostgresPass = value
		case PostgresDB:
			env.PostgresDB = value
		case APIDomain:
			env.APIDomain = value
		default:
			log.Printf("Warning line %d: unknown property in .env: %s=%s", i, key, value)
		}
	}

	return env
}

func getFromEnvironment(environ []string) *Env {
	env := &Env{}

	environment := os.Environ()

	for _, variable := range environment {
		key, value, _ := strings.Cut(variable, "=")

		switch key {
		case PostgresUser:
			env.PostgresUser = value
		case PostgresPass:
			env.PostgresPass = value
		case PostgresDB:
			env.PostgresDB = value
		case APIDomain:
			env.APIDomain = value
		}
	}

	return env
}
