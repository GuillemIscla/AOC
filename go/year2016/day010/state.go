package main

type Bot struct {
	Instruction BotInstruction
	Chips       []int
}

func NewBot(botInstruction BotInstruction) Bot {
	return Bot{
		Instruction: botInstruction,
		Chips:       []int{},
	}
}

func (b *Bot) TakeChip(chip int) error {
	b.Chips = append(b.Chips, chip)
	return nil
}

func (b *Bot) GiveUpHighAndLow() (*int, *int) {
	if len(b.Chips) >= 2 {
		min := b.Chips[0]
		max := b.Chips[0]
		for _, v := range b.Chips[1:] {
			if v < min {
				min = v
			}
			if v > max {
				max = v
			}
		}

		newNums := []int{}
		for _, v := range b.Chips {
			if v != min && v != max {
				newNums = append(newNums, v)
			}
			b.Chips = newNums
		}

		return &min, &max
	} else {
		return nil, nil
	}
}

type Output struct {
	Chips []int
}

func (o *Output) TakeChip(chip int) error {
	o.Chips = append(o.Chips, chip)
	return nil
}

type State struct {
	Bots    []Bot
	Outputs []Output
}

func NewState(inputInstructions []InputInstruction, botInstructions []BotInstruction) State {
	numBots, numOutputs := 0, 0
	for _, instr := range inputInstructions {
		numBots = max(numBots, instr.Bot)
	}
	for _, instr := range botInstructions {
		if instr.LowTo.IsBot {
			numBots = max(numBots, instr.LowTo.ID)
		} else {
			numOutputs = max(numOutputs, instr.LowTo.ID)
		}
		if instr.HighTo.IsBot {
			numBots = max(numBots, instr.HighTo.ID)
		} else {
			numOutputs = max(numOutputs, instr.HighTo.ID)
		}
	}
	bots := make([]Bot, numBots+1)
	for _, instr := range botInstructions {
		bots[instr.Bot] = NewBot(instr)
	}
	return State{
		Bots:    bots,
		Outputs: make([]Output, numOutputs+1),
	}
}
