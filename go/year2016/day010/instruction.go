package main

import (
	"fmt"
	"regexp"
	"strconv"
)

type Instruction interface {
	Apply(s *State, LowChip int, HighChip int) *InstructionResult
}

type Target struct {
	IsBot bool
	ID    int
}

type BotInstruction struct {
	Bot    int
	LowTo  Target
	HighTo Target
}

type InstructionResult struct {
	ChosenBot int
}

func (b BotInstruction) Apply(s *State, LowChip int, HighChip int) *InstructionResult {
	low, high := s.Bots[b.Bot].GiveUpHighAndLow()
	if low != nil && high != nil {
		if b.HighTo.IsBot {
			s.Bots[b.HighTo.ID].TakeChip(*high)
		} else {
			s.Outputs[b.HighTo.ID].TakeChip(*high)
		}
		if b.LowTo.IsBot {
			s.Bots[b.LowTo.ID].TakeChip(*low)
		} else {
			s.Outputs[b.LowTo.ID].TakeChip(*low)
		}

	}
	if *low == LowChip && *high == HighChip {
		result := InstructionResult{
			ChosenBot: b.Bot,
		}
		return &result
	}
	return nil
}

type InputInstruction struct {
	Value int
	Bot   int
}

func (i InputInstruction) Apply(s *State, LowChip int, HighChip int) *InstructionResult {
	s.Bots[i.Bot].TakeChip(i.Value)
	return nil
}

func ParseInstruction(line string) (Instruction, error) {
	var (
		botRe = regexp.MustCompile(
			`^bot (\d+) gives low to (bot|output) (\d+) and high to (bot|output) (\d+)$`,
		)
		valueRe = regexp.MustCompile(
			`^value (\d+) goes to bot (\d+)$`,
		)
	)
	if m := botRe.FindStringSubmatch(line); m != nil {
		bot, _ := strconv.Atoi(m[1])
		lowID, _ := strconv.Atoi(m[3])
		highID, _ := strconv.Atoi(m[5])

		return BotInstruction{
			Bot: bot,
			LowTo: Target{
				IsBot: m[2] == "bot",
				ID:    lowID,
			},
			HighTo: Target{
				IsBot: m[4] == "bot",
				ID:    highID,
			},
		}, nil
	}

	if m := valueRe.FindStringSubmatch(line); m != nil {
		value, _ := strconv.Atoi(m[1])
		bot, _ := strconv.Atoi(m[2])

		return InputInstruction{
			Value: value,
			Bot:   bot,
		}, nil
	}

	return nil, fmt.Errorf("invalid instruction: %q", line)
}
