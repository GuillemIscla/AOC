package main

import (
	"crypto/md5"
	"encoding/hex"
	"fmt"
	"strconv"
)

func part2() string {
	doorId, _ := parseInput(nil)

	password := make([]rune, 8)
	for i := range password {
		password[i] = 'z'
	}
	passwordChar := ' '
	passwordCharPosition := ""
	pad := 0

	for {
		passwordChar, passwordCharPosition, pad = FindHashSecond(doorId, pad+1)
		validPosition, isValid := ValidatePosition(passwordCharPosition)
		if isValid && password[validPosition] == 'z' {
			password[validPosition] = passwordChar
		}
		if PasswordComplete(password) {
			break
		}
	}

	return string(password)
}

func FindHashSecond(doorId string, padding int) (rune, string, int) {
	for {
		hash := md5.Sum([]byte(doorId + fmt.Sprintf("%d", padding)))
		hashStr := hex.EncodeToString(hash[:])
		if hashStr[:5] == "00000" {
			return rune(hashStr[6]), hashStr[5:6], padding + 1
		}
		padding++
	}
}

func ValidatePosition(position string) (int, bool) {
	pos, err := strconv.Atoi(position)
	if err != nil {
		return -1, false
	}
	if pos < 0 || pos > 7 {
		return -1, false
	}
	return pos, true
}

func PasswordComplete(password []rune) bool {
	for _, char := range password {
		if char == 'z' {
			return false
		}
	}
	return true
}
