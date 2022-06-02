def selectByCondition(Object parameter) {
    '''
SELECT id, name, age, addr
FROM `user`
''' + where { c ->
        if (parameter?.id) c += 'id = #{id} '
        if (parameter?.name) c += ' AND name = #{name} '
        c
    }
}

def selectById(Object parameter) {
    '''
SELECT id, name, age, addr
FROM `user`
WHERE id = #{id}
'''
}

def selectByWrapper(Object parameter) {
    '''
SELECT id, name, age, addr
FROM `user`
WHERE id = ${id} AND name = #{name}
'''
}