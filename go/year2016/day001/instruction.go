package main

import (
	"fmt"
	"strconv"
	"strings"
)

type Direction int

const (
	Left Direction = iota
	Right
)

type Instruction struct {
	Direction Direction
	Steps     int
}

func NewInstruction(token string) (Instruction, error) {
	token = strings.TrimSpace(token)
	if len(token) < 2 {
		return Instruction{}, fmt.Errorf("invalid token: %s", token)
	}

	var dir Direction
	switch token[0] {
	case 'R':
		dir = Right
	case 'L':
		dir = Left
	default:
		return Instruction{}, fmt.Errorf("unknown direction: %c", token[0])
	}

	steps, err := strconv.Atoi(token[1:])
	if err != nil {
		return Instruction{}, fmt.Errorf("invalid step count: %s", token[1:])
	}

	return Instruction{Direction: dir, Steps: steps}, nil
}
