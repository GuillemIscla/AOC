package main

func part1() int {
	// v := 1
	//inputInstructions, botInstructions, _ := parseInput(&v)
	lowValueToLook, highValueToLook := 17, 61
	inputInstructions, botInstructions, _ := parseInput(nil)
	state := NewState(inputInstructions, botInstructions)

	for _, instr := range inputInstructions {
		instr.Apply(&state, lowValueToLook, highValueToLook)
	}

	ToCompute := []int{}

	for botId, bot := range state.Bots {
		if len(bot.Chips) >= 2 {
			ToCompute = append(ToCompute, botId)
		}
	}

	for len(ToCompute) > 0 {
		//Pop
		ComputeNow := ToCompute[0]
		ToCompute = ToCompute[1:]

		//Apply
		instruction := state.Bots[ComputeNow].Instruction
		result := instruction.Apply(&state, lowValueToLook, highValueToLook)
		if result != nil {
			return result.ChosenBot
		}

		//Push
		if instruction.LowTo.IsBot && len(state.Bots[instruction.LowTo.ID].Chips) >= 2 {
			ToCompute = append(ToCompute, instruction.LowTo.ID)
		}
		if instruction.HighTo.IsBot && len(state.Bots[instruction.HighTo.ID].Chips) >= 2 {
			ToCompute = append(ToCompute, instruction.HighTo.ID)
		}
	}

	return -1
}
