package main

func part1() int {
	// v := 1
	// favoriteNumber, _ := parseInput(&v)
	favoriteNumber, _ := parseInput(nil)

	maze := NewMaze(favoriteNumber)

	maze.Explore(40, 40)

	origin := [2]int{1, 1}

	destination := [2]int{31, 39}

	path, _ := Dijsktra(maze, origin, destination)

	return len(path) - 1
}

func Dijsktra(maze Maze, origin [2]int, destination [2]int) (Path, bool) {
	queue := []Path{{origin}}

	previousPaths := make(map[[2]int]Path)

	previousPaths[origin] = Path{origin}

	for len(queue) > 0 {
		cur := queue[0]
		queue = queue[1:]

		if cur[len(cur)-1][0] == destination[0] && cur[len(cur)-1][1] == destination[1] {
			return cur, true
		}

		nextPaths := cur.Next(maze)

		for _, nextPath := range nextPaths {
			_, ok := previousPaths[nextPath[len(nextPath)-1]]
			if !ok {
				queue = append(queue, nextPath)
				previousPaths[nextPath[len(nextPath)-1]] = nextPath
			}
		}
	}

	return Path{}, false
}
