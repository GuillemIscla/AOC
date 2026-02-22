package main

func part2() int {
	// v := 1
	// favoriteNumber, _ := parseInput(&v)

	favoriteNumber, _ := parseInput(nil)

	maze := NewMaze(favoriteNumber)

	origin := [2]int{1, 1}

	maxSteps := 50

	locationsCount := DijsktraCountLocations(maze, origin, maxSteps)

	return locationsCount
}

func DijsktraCountLocations(maze Maze, origin [2]int, maxSteps int) int {
	queue := []Path{{origin}}

	previousPaths := make(map[[2]int]Path)

	previousPaths[origin] = Path{origin}

	for len(queue[0]) <= maxSteps {
		cur := queue[0]
		queue = queue[1:]

		nextPaths := cur.Next(maze)

		for _, nextPath := range nextPaths {
			_, ok := previousPaths[nextPath[len(nextPath)-1]]
			if !ok {
				queue = append(queue, nextPath)
				previousPaths[nextPath[len(nextPath)-1]] = nextPath
			}
		}
	}

	return len(previousPaths)
}
