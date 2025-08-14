package main

func part1() int {
	instructions, _ := parseInput(nil)

	PositionLookAt := PositionLookAt{Position: Position{X: 0, Y: 0}, LookAt: North}

	for _, inst := range instructions {
		PositionLookAt.Process(inst)
	}

	return PositionLookAt.Position.DistanceFromCenter()
}
