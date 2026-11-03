package main

import (
	"os"
	"testing"

	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func TestGetEnvFromFile(t *testing.T) {
	for _, tt := range []struct {
		name         string
		fileContents []byte
		expected     *Env
	}{
		{"1 line no line ending", []byte("POSTGRES_PASS=password"), &Env{PostgresPass: "password"}},
		{"1 line with line ending", []byte("POSTGRES_PASS=password\n"), &Env{PostgresPass: "password"}},
		{"2 lines", []byte("POSTGRES_PASS=password\nPOSTGRES_USER=username"), &Env{PostgresUser: "username", PostgresPass: "password"}},
	} {
		t.Run(tt.name, func(t *testing.T) {
			tempFile, err := os.CreateTemp(os.TempDir(), "")
			require.Nil(t, err)

			_, err = tempFile.Write(tt.fileContents)
			require.Nil(t, err)

			require.Nil(t, tempFile.Close())

			actual := getFromFile(tempFile.Name())

			assert.Equal(t, actual, tt.expected)
		})
	}
}
