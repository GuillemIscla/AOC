package main

type Path [][2]int

func (p Path) Next(m Maze) []Path {
	var result []Path
	last := p[len(p)-1]
	for _, option := range [][2]int{
		{last[0] - 1, last[1]},
		{last[0] + 1, last[1]},
		{last[0], last[1] - 1},
		{last[0], last[1] + 1},
	} {
		if option[0] >= 0 && option[1] >= 0 && m.Explore(option[0], option[1]) != '#' {
			pathCopy := make(Path, len(p))
			copy(pathCopy, p)
			pathCopy = append(pathCopy, option)
			result = append(result, pathCopy)
		}
	}
	return result
}
