" fl.vim
syntax case match
syntax keyword flLetIn include in
syntax match flGuards /|\|->>/
syntax match flFunction /->[^>]\|->\n/
syntax region flComment start=/#/ end=/\n/
syntax region flBlockComment start=/###/ end=/###/
syntax region flString start=/"/ skip=/\\"/ end=/"/
syntax match flOperators /+\|*\|\/\|=\|==/
syntax match flBrackets /\[\|]\|{\|}\|,\|;\|:\|(\|)/
" syntax region flAttrs start=/{/ end=/}/ contains=flAttrs

" Linking
highlight link flLetIn Structure 
highlight link flComment Comment
highlight link flFunction Structure 
highlight link flOperators Operator
highlight link flGuards Structure
highlight link flBrackets Special
highlight link flString String
highlight link flBlockComment Comment
