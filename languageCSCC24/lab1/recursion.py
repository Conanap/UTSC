"""Review: some simple exercises on recursion. In Python3.  Please
implement all of the following recursively.  Otherwise it defeats the
purpose of this lab --- we need to freshen up on our recursion skills.

You have until Friday 6p.m. to submit your work on
 https://mathlab.utsc.utoronto.ca/websubmit/students

For full marks, this and every piece of Python code you submit must
pass PEP8 stylechecker and pylint. See course website to get the
software.

"""


def length(lst):
    """(list) -> int

    Return the length of lst.
    """
    if lst != []:
        return length(lst[:-1]) + 1
    return 0


def reverse(lst):
    """(list) -> list

    Return the reverse of lst.

    """
    if len(lst) > 1:
        return reverse(lst[1:]) + [lst[0]]
    return lst


def is_pal(lst):
    """(list) -> bool

    Return whether lst is a palindrome.  For fun, do not use an
    if-statement.

    """
    try:
        return lst[0] == lst[-1] and is_pal(lst[1:-1])
    except IndexError:
        return True


def num_el(lst):
    """(list) -> int

    Return the number of (non-list) elements of lst, on any nesting
    level.

    """
    tot = 0
    if len(lst) == 0:
        return 0
    tot += num_el(lst[1:])
    if isinstance(lst[0], list):
        tot += num_el(lst[0])
    else:
        tot += 1
    return tot


def stutter(lst):
    """(list) -> (list)

    Return a list, which repeats each element of lst twice.

    """
    if len(lst) < 1:
        return lst
    elif len(lst) == 1:
        return lst + [lst[0]]
    else:
        return [lst[0], lst[0]] + stutter(lst[1:])


def my_filter(func, lst):
    """(function, list) -> list

    Return a list of those elements from lst that pass the function
    func (i.e., func(x) is True for element x in lst), in their
    original order.

    func is a boolean-valued function applicable to every element of
    lst.

    """
    if len(lst) == 0:
        return []
    elif len(lst) == 1:
        if func(lst[0]):
            return [lst[0]]
        else:
            return []
    rlst = my_filter(func, lst[1:])
    if func(lst[0]):
        rlst = [lst[0]] + rlst
    return rlst


def my_map(func, lst):
    """(function, list) -> list

    Return the result of applying func to every element of lst.

    Pre: func is applicable to every element of lst.

    """
    if len(lst) > 1:
        return [func(lst[0])] + my_map(func, lst[1:])
    elif len(lst) == 1:
        return [func(lst[0])]
    return []


if __name__ == '__main__':

    pass
