package main

import (
	"errors"
	"fmt"
	"regexp"
	"strconv"
)

type Instruction interface {
	Process(screen *[50][6]bool)
}

type Rect struct {
	Width  int
	Height int
}

func (r Rect) String() string {
	return fmt.Sprintf("rect %dx%d", r.Width, r.Height)
}

func (r Rect) Process(screen *[50][6]bool) {
	for y := 0; y < r.Height; y++ {
		for x := 0; x < r.Width; x++ {
			(*screen)[x][y] = true
		}
	}
}

type RotateRow struct {
	Row   int
	Shift int
}

func (r RotateRow) Process(screen *[50][6]bool) {
	row := r.Row
	shift := r.Shift % 50
	originalRow := make([]bool, 50)
	for i := 0; i < 50; i++ {
		originalRow[i] = (*screen)[i][row]
	}
	for x := 0; x < 50; x++ {
		(*screen)[(x+shift)%50][row] = originalRow[x]
	}
}

func (r RotateRow) String() string {
	return fmt.Sprintf("rotate row y=%d by %d", r.Row, r.Shift)
}

type RotateColumn struct {
	Column int
	Shift  int
}

func (r RotateColumn) Process(screen *[50][6]bool) {
	column := r.Column
	shift := r.Shift % 6
	originalColumn := make([]bool, 6)
	for y := 0; y < 6; y++ {
		originalColumn[y] = (*screen)[column][y]
	}
	for y := 0; y < 6; y++ {
		(*screen)[column][(y+shift)%6] = originalColumn[y]
	}
}

func (r RotateColumn) String() string {
	return fmt.Sprintf("rotate column x=%d by %d", r.Column, r.Shift)
}

func NewInstruction(line string) (Instruction, error) {

	rect, rectErr := NewRect(line)
	if rectErr == nil {
		return rect, nil
	}

	rotateRow, rowErr := NewRotateRow(line)
	if rowErr == nil {
		return rotateRow, nil
	}

	rotateColumn, colErr := NewRotateColumn(line)
	if colErr == nil {
		return rotateColumn, nil
	}

	return nil, errors.Join(rectErr, rowErr, colErr)
}

func NewRect(line string) (Rect, error) {
	re := regexp.MustCompile(`rect ([0-9]+)x([0-9]+)`)
	matches := re.FindStringSubmatch(line)
	if len(matches) != 3 {
		return Rect{}, errors.New("invalid rectangle instruction")
	}
	width, _ := strconv.Atoi(matches[1])
	height, _ := strconv.Atoi(matches[2])
	return Rect{Width: width, Height: height}, nil
}

func NewRotateRow(line string) (RotateRow, error) {
	re := regexp.MustCompile(`rotate row y=([0-9]+) by ([0-9]+)`)
	matches := re.FindStringSubmatch(line)
	if len(matches) != 3 {
		return RotateRow{}, errors.New("invalid rotate row instruction")
	}
	row, _ := strconv.Atoi(matches[1])
	shift, _ := strconv.Atoi(matches[2])
	return RotateRow{Row: row, Shift: shift}, nil
}

func NewRotateColumn(line string) (RotateColumn, error) {
	re := regexp.MustCompile(`rotate column x=([0-9]+) by ([0-9]+)`)
	matches := re.FindStringSubmatch(line)
	if len(matches) != 3 {
		return RotateColumn{}, errors.New("invalid rotate column instruction")
	}
	column, _ := strconv.Atoi(matches[1])
	shift, _ := strconv.Atoi(matches[2])
	return RotateColumn{Column: column, Shift: shift}, nil
}
