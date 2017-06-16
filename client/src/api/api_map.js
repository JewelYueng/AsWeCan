
const base_url = 'http://wemeet.tech:8081'

const
    GET = 'get',
    HEAD = 'head',
    DELETE = 'delete',
    POST = 'post',
    PUT = 'put',
    PATCH = 'patch'

export default {
    __base_url__: base_url,
    'test': [POST, '/team/delete_job/?jobId=1'],
}