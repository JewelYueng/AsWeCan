const base_url = ''

const
  GET = 'get',
  HEAD = 'head',
  DELETE = 'delete',
  POST = 'post',
  PUT = 'put',
  PATCH = 'patch'

module.exports = {
  __baseurl__: base_url,
  'getRawLog': [GET, '/rawLog']
}
