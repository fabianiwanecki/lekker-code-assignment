local key = KEYS[1];
local values = ARGV;
local res = {};

for i, value in ipairs(values) do
    res[i] = redis.call('ZREVRANK', key, value);
end;

return res;