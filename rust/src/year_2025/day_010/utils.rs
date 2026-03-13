pub fn combinations(n: usize, m: usize) -> Vec<Vec<usize>> {
    fn backtrack(
        start: usize,
        n: usize,
        m: usize,
        current: &mut Vec<usize>,
        result: &mut Vec<Vec<usize>>,
    ) {
        if current.len() == m {
            result.push(current.clone());
            return;
        }

        for i in start..n {
            current.push(i);
            backtrack(i + 1, n, m, current, result);
            current.pop();
        }
    }

    let mut result = Vec::new();
    let mut current = Vec::new();
    backtrack(0, n, m, &mut current, &mut result);
    result
}
