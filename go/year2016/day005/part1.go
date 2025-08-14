package main

import (
	"crypto/md5"
	"encoding/hex"
	"fmt"
)

func part1() string {
	doorId, _ := parseInput(nil)

	password := ""
	passwordChar := ""
	pad := 0

	for i := 0; i < 8; i++ {
		passwordChar, pad = FindHash(doorId, pad+1)
		password += passwordChar
	}

	return password
}

func FindHash(doorId string, padding int) (string, int) {
	for {
		hash := md5.Sum([]byte(doorId + fmt.Sprintf("%d", padding)))
		hashStr := hex.EncodeToString(hash[:])
		if hashStr[:5] == "00000" {
			return hashStr[5:6], padding + 1
		}
		padding++
	}
}
