package main

func part2() int {
	instructions, _ := parseInput(nil)

	PositionLookAt := PositionLookAt{Position: Position{X: 0, Y: 0}, LookAt: North}

	positionSet := make(map[Position]bool)

	for _, inst := range instructions {
		before := PositionLookAt.Position
		PositionLookAt.Process(inst)
		after := PositionLookAt.Position
		path := before.BuildPath(after)
		for _, pos := range path {
			if positionSet[pos] {
				return pos.DistanceFromCenter()
			}
			positionSet[pos] = true
		}
	}

	return PositionLookAt.Position.DistanceFromCenter()
}
