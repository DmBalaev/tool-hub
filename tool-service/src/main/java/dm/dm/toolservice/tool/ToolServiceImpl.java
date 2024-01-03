package dm.dm.toolservice.tool;

import dm.dm.toolservice.account.Account;
import dm.dm.toolservice.account.AccountRepository;
import dm.dm.toolservice.exception.EntityNotFoundException;
import dm.dm.toolservice.exception.NotHavePermissions;
import dm.dm.toolservice.tool.dto.ToolDto;
import dm.dm.toolservice.tool.dto.ToolDtoCreate;
import dm.dm.toolservice.tool.dto.ToolDtoUpdate;
import dm.dm.toolservice.tool.dto.ToolMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ToolServiceImpl implements ToolService {
    private final ToolRepository toolRepository;
    private final AccountRepository accountRepository;
    private final ToolMapper toolMapper;

    @Override
    public List<ToolDto> tools(Pageable pageable) {
        return null;
    }

    @Override
    public ToolDto getById(long id) {
        return toolMapper.apply(getTool(id));
    }

    @Override
    public ToolDto addNewTool(ToolDtoCreate dto, UserDetails userDetails) {
        log.info("Account with name:{} added new tool", userDetails.getUsername());
        var currentUser = getAccount(userDetails);
        var entity = Tool.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .category(dto.getCategory())
                .owner(currentUser)
                .build();

        return toolMapper.apply(toolRepository.save(entity));
    }


    @Override
    public ToolDto updateTool(long toolId, ToolDtoUpdate dto, UserDetails userDetails) {
        log.info("Account with name:{} update tool with id:{}", userDetails.getUsername(), toolId);
        var tool = getTool(toolId);

        if (isNotOwner(userDetails, tool)) {
            throw new NotHavePermissions("You can't update, you are not the owner");
        }
        //Todo: расмотреть вариант передавать Map<String,Object>update итерироваться по нему и обновлять при помощи switch/case
        if (dto.getPrice() != null) {
            tool.setPrice(dto.getPrice());
        }
        if (dto.getName() != null) {
            tool.setName(dto.getName());
        }

        return toolMapper.apply(toolRepository.save(tool));
    }

    @Override
    public void delete(Long toolId, UserDetails userDetails) {
        log.info("Account with name:{} delete tool with id:{}", userDetails.getUsername(), toolId);
        var tool = getTool(toolId);
        if (isNotOwner(userDetails, tool)) {
            throw new NotHavePermissions("You can't delete tool, you are not the owner");
        }
        toolRepository.delete(tool);
    }

    private boolean isNotOwner(UserDetails userDetails, Tool tool) {
        return !tool.getOwner().getEmail().equals(userDetails.getUsername());
    }

    private Account getAccount(UserDetails userDetails) {
        return accountRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Account with email:%s not found", userDetails.getUsername())));
    }

    private Tool getTool(long id) {
        return toolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Tool with id:%s not found", id)));
    }
}
