pub fn next_line_iterative(line: &[usize]) -> Vec<usize> {
    let mut result = Vec::with_capacity(line.len() * 2);
    let mut i = 0;

    while i < line.len() {
        let first_in_block = line[i];
        let mut block_size = 0;
        while i + block_size < line.len() && first_in_block == line[i + block_size] {
            block_size += 1;
        }
        result.push(block_size);
        result.push(first_in_block);
        i += block_size;
    }
    result
}

//Since Rust does not support tail recursion, this causes stackoverflow
#[allow(dead_code)]
pub fn next_line_recursive(line: &[usize]) -> Vec<usize> {
    let mut result = Vec::with_capacity(line.len() * 2);
    next_line_recursive_internal(line, &mut result);
    result
}

#[allow(dead_code)]
fn next_line_recursive_internal(line: &[usize], result: &mut Vec<usize>) -> Vec<usize> {
    if line.is_empty() {
        Vec::new()
    } else {
        let first_in_block = line[0];
        let mut block_size: usize = 0;

        while block_size < line.len() && line[block_size] == first_in_block {
            block_size += 1;
        }
        result.push(block_size);
        result.push(first_in_block);
        next_line_recursive_internal(&line[block_size..], result)
    }
}
